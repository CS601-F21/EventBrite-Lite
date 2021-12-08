const SearchBar = (props) => {
  return (
    <div className="searchWrapper">
      <button className="resetSearch searchComponent">All Events</button>
      <input
        className="searchByName searchComponent"
        placeholder="Search By Name"
        type="text"
      ></input>
      <input
        className="searchByLocation searchComponent"
        placeholder="Search By Location"
        type="text"
      ></input>
      <input
        className="searchByPrice searchComponent"
        placeholder="Price Less Than"
        type="number"
        min="0"
      ></input>
      <button className="submitSearch searchComponent">Go</button>
    </div>
  );
};

export default SearchBar;
