/**
 * Author : Shubham
 * Purpose : Second highest level component we have, we instantiate the states over here as well
 */

import "./App.css";
import React, { Component } from "react";
import LoginComponent from "./Components/LoginComponent";
import HomePage from "./Components/HomePage";
import { Route, Routes, Link } from "react-router-dom";
import UserInfo from "./Components/UserInfo";

function App() {
  //the user state, setUser is a function to change the user state, initial value is null
  const [user, setUser] = React.useState(null);
  //the events state, setEvent is a function to change the user state, initial value is an empty array
  const [events, setEvents] = React.useState([]);
  const [displayEvents, setDisplayEvents] = React.useState([]);

  /**
   * Every time the page is loaded, we fetch all the events we are hosting from our server
   */
  React.useEffect(() => {
    fetch("http://localhost:8080/allevents")
      .then((res) => res.json())
      .then((json) => setEvents(json));
  }, []);

  // console.log(events);

  //since ours is a multi-page website, react-router is used to set the routes and to decide which page to render
  //at which route
  //passing the appropriate props to each component as well
  return (
    <Routes>
      <Route
        path="/"
        element={
          <HomePage
            events={events}
            setEvents={setEvents}
            user={user}
            setUser={setUser}
          />
        }
      />

      <Route
        path="/user"
        element={
          <UserInfo
            events={events}
            setEvents={setEvents}
            user={user}
            setUser={setUser}
          />
        }
      />

      {/* <Route path="/user" element={<HomePage />} /> */}
    </Routes>
  );
}

export default App;
