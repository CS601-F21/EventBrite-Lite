/**
 * Author : Shubham
 * Purpose : Component to show the user info
 */
import UserNameAndNameChangePanel from "./UserNameAndNameChangePanel";
import CreateEventPanel from "./CreateEventPanel";
import React from "react";

const UserInfoPane = (props) => {
  //component is broken down into two parts, the username and name change panel and the create events panel
  const sessionId = props.sessionId;
  return (
    <div className="userInfoPane">
      <UserNameAndNameChangePanel sessionId={sessionId} />
      <CreateEventPanel sessionId={sessionId} />
    </div>
  );
};

export default UserInfoPane;
