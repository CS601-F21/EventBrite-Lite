const UserNameChangePanel = (props) => {
  function changePreferredName(event) {
    event.preventDefault();
    const preferredName =
      document.getElementsByClassName("changeNameInput")[0].value;
    console.log(
      `user wants ${preferredName} and sessionid is ${props.sessionId}`
    );
    const sessionId = props.sessionId;

    // /updateusername?sessionid={sessionid}&pref={newname}

    fetch(
      "http://localhost:8080/updateusername?sessionid=" +
        sessionId +
        "&pref=" +
        preferredName,
      {
        method: "POST",
        // mode: "no-cors",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true",
        },
      }
    )
      .then((res) => res.json())
      .then((json) => {
        let res = json[0];
        console.log(res);
        if (res.ok == "true") {
          localStorage.setItem("preferredName", preferredName);
          window.location.reload();
        } else {
          alert("Unable to change display name");
        }
      });
  }

  return (
    <div className="changeNameDiv">
      <span className="changeNameSpan">
        <form onSubmit={changePreferredName}>
          <input
            className="changeNameInput"
            placeholder="Enter new preferred name"
            required
          ></input>
          <button className="changeNameSubmitButton">Go</button>
        </form>
      </span>
    </div>
  );
};

export default UserNameChangePanel;
