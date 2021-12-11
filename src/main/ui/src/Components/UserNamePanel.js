const UserNamePanel = (props) => {
  return (
    <div className="userNamePanel">
      <h3 className="nameText">
        Hello {localStorage.getItem("preferredName")}
      </h3>
      <form action="/">
        <input type="submit" value="Home" className="homeButton" />
      </form>
    </div>
  );
};

export default UserNamePanel;
