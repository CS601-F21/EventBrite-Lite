import UserNamePanel from "./UserNamePanel";
import UserNameChangePanel from "./UserNameChangePanel";
const UserNameAndNameChangePanel = (props) => {
  return (
    <div className="userNameAndNameChangePane">
      <UserNamePanel />
      <UserNameChangePanel sessionId={props.sessionId} />
    </div>
  );
};

export default UserNameAndNameChangePanel;
