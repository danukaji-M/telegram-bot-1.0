from flask import Blueprint, request, jsonify
from app.bot import send_otp, verify_otp, get_account_data, create_group, create_private_channel
import asyncio

bot_blueprint = Blueprint("bot", __name__)

@bot_blueprint.route("/ping", methods=["GET"])
def ping_route():
    return jsonify({"status": "success", "message": "Pong!"})

@bot_blueprint.route("/send-otp", methods=["POST"])
def send_otp_route():
    data = request.get_json()
    phone_number = data.get("phone_number")

    if not phone_number:
        return jsonify({"status": "error", "message": "Phone number is required"}), 400

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    result = loop.run_until_complete(send_otp(phone_number))
    return jsonify(result)

@bot_blueprint.route("/verify-otp", methods=["POST"])
def verify_otp_route():
    data = request.get_json()
    phone_number = data.get("phone_number")
    code = data.get("code")
    password = data.get("password")

    if not phone_number or not code:
        return jsonify({"status": "error", "message": "Phone number and code are required"}), 400

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    result = loop.run_until_complete(verify_otp(phone_number, code, password))
    return jsonify(result)

@bot_blueprint.route("/get-account-data", methods=["POST"])
def get_account_data_route():
    data = request.get_json()
    phone_number = data.get("phone_number")

    if not phone_number:
        return jsonify({"status": "error", "message": "Phone number is required"}), 400

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    result = loop.run_until_complete(get_account_data(phone_number))
    return jsonify(result)

@bot_blueprint.route("/create-group", methods=["POST"])
def create_group_route():
    data = request.get_json()
    phone_number = data.get("phone_number")
    group_name = data.get("group_name")
    users = data.get("users")  # List of user IDs to add to the group

    if not phone_number or not group_name:
        return jsonify({"status": "error", "message": "Phone number, group name, and users are required"}), 400

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    result = loop.run_until_complete(create_group(phone_number, group_name,users))
    return jsonify(result)

@bot_blueprint.route("/create-channel", methods=["POST"])
def create_channel_route():
    data = request.get_json()
    phone_number = data.get("phone_number")
    channel_name = data.get("channel_name")
    users = data.get("users", [])  # Default to an empty list if not provided
    photo = data.get("photo")
    about = data.get("about")

    try:
        # Use asyncio to run the async function
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
        result = loop.run_until_complete(
            create_private_channel(phone_number, channel_name, users, photo, about)
        )
        return result  # Assuming create_private_channel returns a JSON response
    except Exception as e:
        # Handle errors gracefully
        return jsonify({"status": "error", "message": str(e)}), 500
    finally:
        # Ensure the loop is closed
        loop.close()
