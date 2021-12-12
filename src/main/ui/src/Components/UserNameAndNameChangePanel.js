/**
 * Author : Shubham
 * Purpose : Component to show the user name and let the user change their name
 */
import UserNamePanel from "./UserNamePanel";
import UserNameChangePanel from "./UserNameChangePanel";
const UserNameAndNameChangePanel = (props) => {
  //component is broken into two parts, to show the user their name and to let them change their preferredName
  return (
    <div className="userNameAndNameChangePane">
      <UserNamePanel />
      <UserNameChangePanel sessionId={props.sessionId} />
    </div>
  );
};

export default UserNameAndNameChangePanel;
