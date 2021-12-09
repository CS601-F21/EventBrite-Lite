import LoginComponent from "./LoginComponent";
import LogoutComponent from "./LogoutComponent";
import SearchBar from "./SearchBar";

const TopPane = (props) => {
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
        <LogoutComponent user = {props.user} setUser = {props.setUser} />
        <SearchBar events={props.events} setEvents={props.setEvents} />
      </div>
    );
  }
};

export default TopPane;
