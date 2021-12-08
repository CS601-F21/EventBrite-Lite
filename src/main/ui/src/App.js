import "./App.css";
import React, { Component } from "react";
/**
 * Import all pages for different routes over here
 */
import LoginComponent from "./Components/LoginComponent";
import HomePage from "./Components/HomePage";
import { Route, Routes, Link } from "react-router-dom";

function App() {
  const [user, setUser] = React.useState(null);
  const [events, setEvents] = React.useState([]);
  const [displayEvents, setDisplayEvents] = React.useState(events);

  React.useEffect(() => {
    fetch("http://localhost:8080/allevents")
      .then((res) => res.json())
      .then((json) => setEvents(json));
  }, []);

  React.useEffect(() => {
    // get user session
    setUser();
  }, []);

  React.useEffect(() => {
    // api call to transfer ticket
    // setEvents(retrievedEvents);
  }, []);

  console.log(events);

  return (
    <Routes>
      {/* <Route path = "/login" element = {<LoginPage user={user} />}/> */}
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

      <Route path="/user" element={<HomePage />} />
    </Routes>
  );
}

export default App;
