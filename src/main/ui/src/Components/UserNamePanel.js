const UserNamePanel = (props) => {
    return (
        <div className = "userNamePanel">
            <h3 className = "nameText">Hello {localStorage.getItem("preferredName")}</h3>
        </div>
    )
}

export default UserNamePanel;