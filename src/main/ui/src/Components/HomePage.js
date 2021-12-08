import react, { Component, useState } from "react";
import { useSelector } from "react-redux";
import React from "react";
import LoginComponent from "./LoginComponent";
import TopPane from "./TopPane";
import LowerPane from "./LowerPane";

const HomePage = (props) => {
  const { events, setEvents } = props;
  // console.log("events", events);

  function addEvent() {
    // make api call
    // get response

    fetch("http://localhost:8080/allevents")
      .then((res) => res.json())
      .then((json) => setEvents([...props.events, ...json]));
  }
  return (
    <div className="homePage">
      <TopPane />
      <LowerPane />
    </div>
  );
};

export default HomePage;
