from flask import Flask # type: ignore
from flask_cors import CORS # type: ignore
from app.routes import bot_blueprint

app = Flask(__name__)
CORS(app)

# Register Blueprints
app.register_blueprint(bot_blueprint)

if __name__ == "__main__":
    app.run(debug=True)
