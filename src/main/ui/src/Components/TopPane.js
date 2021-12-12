/**
 * Author : Shubham Pareek
 * Purpose : Top Pane of the Home Page
 */
import LoginComponent from "./LoginComponent";
import LogoutComponent from "./LogoutComponent";
import SearchBar from "./SearchBar";

const TopPane = (props) => {
  //checking whether we have the user info in the local storage, if so that means that the user is authenticated
  //depending on whether the user is authenticated or not we display different HTML
  if (localStorage.getItem("firstName") == null) {
    return (
      <div className="topPane">
        <LoginComponent />
        <SearchBar events={props.events} setEvents={props.setEvents} />
      </div>
    );
  } else {
    return (
      <div className="topPane">
        <LogoutComponent user={props.user} setUser={props.setUser} />
        <SearchBar events={props.events} setEvents={props.setEvents} />
      </div>
    );
  }
};

export default TopPane;
