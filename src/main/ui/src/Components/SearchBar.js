/**
 * Author : Shubham Pareek
 * Purpose : Component for the search bar
 */
const SearchBar = (props) => {
  const events = props.events;
  const setEvents = props.setEvents;

  //function will get executed if the user clicks the all events button
  function getAllEvents() {
    /**return all events */
    fetch("http://localhost:8080/allevents")
      .then((res) => res.json())
      .then((json) => setEvents([...json]));
  }

  //function will get executed if the user wants to search for a particular events
  function searchForEvents() {
    //getting the users search queries
    let containsExactWord =
      document.getElementsByClassName("searchByName")[0].value;
    let location = document.getElementsByClassName("searchByLocation")[0].value;
    let priceLessThan =
      document.getElementsByClassName("searchByPrice")[0].value;

    if (priceLessThan == "") {
      priceLessThan = 0;
    }
    console.log(
      `contains word ${containsExactWord} -> location ${location} -> price less than ${priceLessThan}`
    );

    //making the fetch request
    fetch(
      "http://localhost:8080/search?word=" +
        containsExactWord +
        "&location=" +
        location +
        "&price=" +
        priceLessThan,
      {
        method: "GET",
        // mode: "no-cors",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true",
        },
        // body: JSON.stringify({
        //   containsExactWord: containsExactWord,
        //   location: location,
        //   priceLessThan: priceLessThan,
        // }),
      }
    )
      .then((res) => res.json())
      .then((json) => setEvents([...json]));
  }

  return (
    <div className="searchWrapper">
      <button className="resetSearch searchComponent" onClick={getAllEvents}>
        All Events
      </button>
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
      <button
        className="submitSearch searchComponent"
        onClick={searchForEvents}
      >
        Go
      </button>
    </div>
  );
};

export default SearchBar;
