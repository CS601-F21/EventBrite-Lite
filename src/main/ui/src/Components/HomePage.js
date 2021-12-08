import react, { Component, useState } from "react";
import { useSelector } from "react-redux";
import React from "react";

const HomePage = (props) => {
  const { events, setEvents } = props;
  console.log("events", events);

  function addEvent() {
    // make api call
    // get response

    fetch("http://localhost:8080/allevents")
      .then((res) => res.json())
      .then((json) => setEvents([...props.events, ...json]));
  }

  return (
    <div>
      <div>
        {props.events.map((e) => (
          <p>{e.Name}</p>
        ))}
      </div>
      <div style={{ cursor: "pointer" }} onClick={() => props.setEvents([])}>
        set events
      </div>
      <button onClick={addEvent}>add event</button>
    </div>
  );
};

export default HomePage;
