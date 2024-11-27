import React, { useState } from "react";
import axios from "axios";

const AccountData = () => {
    const [phoneNumber, setPhoneNumber] = useState("");
    const [accountData, setAccountData] = useState(null);
    const [errorMessage, setErrorMessage] = useState("");

    const fetchAccountData = async () => {
        try {
            const response = await axios.post("http://127.0.0.1:5000/get-account-data", {
                phone_number: phoneNumber,
            });

            if (response.data.status === "success") {
                setAccountData(response.data.account_data);
                setErrorMessage("");  // Clear any previous errors
            } else {
                setErrorMessage(response.data.message);
            }
        } catch (error) {
            setErrorMessage("Error fetching account data!");
        }
    };

    return (
        <div>
            <h1>Account Data</h1>
            <input
                type="text"
                placeholder="Enter your phone number"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
            />
            <button onClick={fetchAccountData}>Get Account Info</button>

            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}

            {accountData && (
                <div>
                    <h2>Account Information</h2>
                    <p><strong>Username:</strong> {accountData.username}</p>
                    <p><strong>Phone Number:</strong> {accountData.phone}</p>
                    <p><strong>Name:</strong> {accountData.name}</p>
                    <p><strong>User ID:</strong> {accountData.user_id}</p>
                </div>
            )}
        </div>
    );
};

export default AccountData;
