import LoginComponent from "./LoginComponent";
import LogoutComponent from "./LogoutComponent";
import SearchBar from "./SearchBar";

const TopPane = (props) => {
  if (localStorage.getItem("firstName") == null) {
    return (
      <div className="topPane">
        <LoginComponent />
        <SearchBar />
      </div>
    );
  } else {
    return (
      <div className="topPane">
        <LogoutComponent />
        <SearchBar />
      </div>
    );
  }
};

export default TopPane;
