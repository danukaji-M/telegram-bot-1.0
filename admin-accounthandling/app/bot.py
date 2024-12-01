from telethon import TelegramClient
from telethon.tl.functions.channels import CreateChannelRequest
from telethon.tl.functions.messages import EditChatDefaultBannedRightsRequest
from telethon.tl.types import ChatBannedRights
from telethon.tl.functions.photos import UploadProfilePhotoRequest
from telethon.tl.functions.channels import EditPhotoRequest
from telethon.tl.types import InputPeerUser, InputChatUploadedPhoto,ChatAdminRights, Channel
from telethon.errors import SessionPasswordNeededError
from telethon.tl.functions.messages import ExportChatInviteRequest
from telethon.tl.functions.channels import InviteToChannelRequest, EditAdminRequest
from telethon.tl.types import InputPeerUser
from telethon.errors import UserAlreadyParticipantError, ChatAdminRequiredError, UserPrivacyRestrictedError, UserNotMutualContactError
from telethon.tl.types import ChatAdminRights
from telethon.tl.functions.channels import CreateChannelRequest, EditPhotoRequest, InviteToChannelRequest, EditAdminRequest
from telethon.tl.types import InputChatUploadedPhoto, ChatAdminRights
from flask import jsonify
import os
import requests 

# Telegram API credentials
API_ID = 24150008  # Set your API_ID
API_HASH = "b63bf4acbec8984467850b3474d218ef"  # Set your API_HASH


async def send_otp(phone_number):
    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.start()  # Use start() to handle connection and authorization

    if not await client.is_user_authorized():
        try:
            otp_sent = await client.send_code_request(phone_number)
            return {"status": "success", "otp_type": otp_sent.type}
        except Exception as e:
            return {"status": "error", "message": str(e)}
    else:
        return {"status": "already_authorized"}

async def verify_otp(phone_number, code, password=None):
    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.start()  # Use start() to handle connection and authorization

    try:
        await client.sign_in(phone=phone_number, code=code)
        return {"status": "success"}
    except SessionPasswordNeededError:
        if password:
            try:
                await client.sign_in(password=password)
                return {"status": "success"}
            except Exception as e:
                return {"status": "error", "message": str(e)}
        return {"status": "2fa_required"}
    except Exception as e:
        return {"status": "error", "message": str(e)}

async def get_account_data(phone_number):
    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.connect()

    if await client.is_user_authorized():
        # Fetch account data (e.g., username, phone, name, etc.)
        user = await client.get_me()
        account_data = {
            "username": user.username,
            "phone": user.phone,
            "name": user.first_name + " " + (user.last_name or ""),
            "user_id": user.id
        }
        await client.disconnect()
        return {"status": "success", "account_data": account_data}
    else:
        await client.disconnect()
        return {"status": "error", "message": "User not authorized. Please log in first."}

async def create_group(phone_number, group_name, users, photo_path=None):

    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.connect()

    if await client.is_user_authorized():
        try:
            # Create the group
            result = await client(CreateChannelRequest(
                title=group_name,
                about="This is a RUU-Films Franchise Group",
                megagroup=True,
            ))
            group = result.chats[0]

            # Disable default permissions
            banned_rights = ChatBannedRights(
                until_date=None,
                send_messages=True,
                send_media=True,
                send_stickers=True,
                send_gifs=True,
                send_games=True,
                send_inline=True,
                embed_links=True,
                send_polls=True,
                change_info=True,
                invite_users=True,
                pin_messages=True,
            )
            await client(EditChatDefaultBannedRightsRequest(group.id, banned_rights))

            # Upload a profile picture (if provided)
            if photo_path:
                try:
                    upload_photo = await client.upload_file(photo_path)
                    await client(EditPhotoRequest(group.id, InputChatUploadedPhoto(upload_photo)))
                except Exception as e:
                    print(f"Failed to set group photo: {str(e)}")

            # Add default bots and other users
            valid_users = []
            bot_ids = [7960468509, 1951601939, 1394530085]  
            for bot_id in bot_ids:
                try:
                    bot_entity = await client.get_input_entity(bot_id)
                    valid_users.append(bot_entity)
                except Exception as e:
                    print(f"Error processing bot with ID {bot_id}: {str(e)}")

            for user in users:
                try:
                    user_entity = await client.get_input_entity(user)
                    valid_users.append(user_entity)
                except Exception as e:
                    print(f"Error processing user {user}: {str(e)}")

            # Add users to the group
            if valid_users:
                try:
                    await client(InviteToChannelRequest(group.id, valid_users))
                except UserAlreadyParticipantError:
                    print("Some users are already part of the group.")
                except Exception as e:
                    print(f"Error adding users: {str(e)}")

            # Grant admin rights to both bots
            admin_rights = ChatAdminRights(
                change_info=True,
                post_messages=True,
                edit_messages=True,
                delete_messages=True,
                ban_users=True,
                invite_users=True,
                pin_messages=True,
                add_admins=False,  # Adjust as needed
                manage_call=True,
            )
            for bot_id in bot_ids:
                try:
                    bot_entity = await client.get_input_entity(bot_id)
                    await client(EditAdminRequest(group.id, bot_entity, admin_rights, f"Admin Bot {bot_id}"))
                except Exception as e:
                    print(f"Failed to grant admin rights to bot {bot_id}: {str(e)}")

            # Generate an invite link
            invite = await client(ExportChatInviteRequest(group.id))
            group_link = invite.link

            group_data = {
                "group_name": group.title,
                "group_id": group.id,
                "group_link": group_link
            }

            await client.disconnect()
            return {"status": "success", "group_data": group_data}

        except Exception as e:
            await client.disconnect()
            return {"status": "error", "message": f"Failed to create group: {str(e)}"}
    else:
        await client.disconnect()
        return {"status": "error", "message": "User not authorized. Please log in first."}


async def create_private_channel(phone_number, channel_name, users, photo_path=None, channel_about=None):
    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.connect()

    if not await client.is_user_authorized():
        await client.disconnect()
        return {"status": "error", "message": "User not authorized. Please log in first."}

    try:
        # Create the private channel
        result = await client(CreateChannelRequest(
            title=channel_name,
            about=channel_about or "",
            megagroup=False
        ))
        channel = result.chats[0]

        # Upload a profile picture if provided
        if photo_path:
            try:
                if photo_path.startswith("http"):
                    response = requests.get(photo_path)
                    if response.status_code == 200:
                        with open("temp_photo.jpg", "wb") as f:
                            f.write(response.content)
                        upload_photo = await client.upload_file("temp_photo.jpg")
                        os.remove("temp_photo.jpg")  # Clean up the temporary file
                    else:
                        raise Exception(f"Failed to download image from {photo_path}")
                else:
                    # Upload directly if it's a local file path
                    upload_photo = await client.upload_file(photo_path)

                await client(EditPhotoRequest(channel.id, InputChatUploadedPhoto(upload_photo)))
            except Exception as e:
                print(f"Failed to set channel photo: {str(e)}")


        # Generate an invite link
        try:
            invite = await client(ExportChatInviteRequest(channel.id))
            channel_link = invite.link
        except Exception as e:
            print(f"Failed to generate invite link: {str(e)}")
            channel_link = None

        # Add bots and users
        bot_ids = ["@ruufillmsupload_bot","@AntiServiceMessage_Bot"]
        for user_id in bot_ids:
            await add_users_to_channel(channel_link,user_id, client)

        channel_data = {
            "channel_name": channel_name,
            "channel_id": channel.id,
            "channel_link": channel_link
        }

        await client.disconnect()
        return {"status": "success", "channel_data": channel_data}
    except Exception as e:
        await client.disconnect()
        return {"status": "error", "message": str(e)}

async def add_users_to_channel(channel_link, bot_username, client):
    try:
        # Get channel or group entity
        bot = await client.get_entity(bot_username)
        target = await client.get_entity(channel_link)
        
        if not isinstance(target, Channel):
            print("The target is not a channel. Cannot add bot as Admin")
            return
        # Check if the target is a channel
        if target.megagroup:  # This means it's a group
            print("The target is a group. Bots cannot be admins in groups.")
            return

        # Invite bot to channel

        # Set admin rights
        admin_rights = ChatAdminRights(
            change_info=True,
            post_messages=True,
            edit_messages=True,
            delete_messages=True,
            ban_users=True,
            invite_users=True,
            pin_messages=True,
            add_admins=False,  # Optional
            manage_call=True
        )
        await client(EditAdminRequest(
            channel=target,
            user_id=bot,
            admin_rights=admin_rights,
            rank="Admin"
        ))
        await client(InviteToChannelRequest(target,[bot]))
        print(f"Successfully invited {bot_username} to the channel.")

        print(f"Successfully granted admin rights to {bot_username} in the channel.")
    except Exception as e:
        print(f"Failed to add user and grant admin rights: {e}")
