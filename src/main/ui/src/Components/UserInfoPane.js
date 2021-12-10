import UserNameAndNameChangePanel from  "./UserNameAndNameChangePanel";
import CreateEventPanel from "./CreateEventPanel";
import React from "react";

const UserInfoPane = (props) => {
    const sessionId = props.sessionId; 
    return (
        <div className = "userInfoPane">
            <UserNameAndNameChangePanel sessionId = {sessionId}/>
            <CreateEventPanel sessionId = {sessionId}/>
        </div>
    )
}

export default UserInfoPane;