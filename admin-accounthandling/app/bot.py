from telethon import TelegramClient
from telethon.tl.functions.channels import CreateChannelRequest
from telethon.tl.functions.messages import EditChatDefaultBannedRightsRequest
from telethon.tl.types import ChatBannedRights
from telethon.tl.types import InputPeerUser
from telethon.errors import SessionPasswordNeededError
import os

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
    
async def create_group(phone_number, group_name):
    """
    Create a new group with the given group name, without adding any users.
    """
    client = TelegramClient(f"sessions/{phone_number}", API_ID, API_HASH)
    await client.connect()

    if await client.is_user_authorized():
        try:
            # Create the chat (group)
            result = await client(CreateChannelRequest(
                title=group_name,
                about="This is a RUU-Films Franchise Group",  # Modify this as needed
                megagroup=True,  # For supergroups
            ))

            group = result.chats[0]

            # Disable all permissions for default users
            banned_rights = ChatBannedRights(
                until_date=None,  # Permanent ban
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

            group_data = {
                "group_name": group.title,
                "group_id": group.id
            }

            await client.disconnect()
            return {"status": "success", "group_data": group_data}

        except Exception as e:
            await client.disconnect()
            return {"status": "error", "message": f"Failed to create group: {str(e)}"}

    else:
        await client.disconnect()
        return {"status": "error", "message": "User not authorized. Please log in first."}