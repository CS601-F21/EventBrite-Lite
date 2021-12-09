import react, { Component, useState } from "react";
import { useSelector } from "react-redux";
import React from "react";
import LoginComponent from "./LoginComponent";
import TopPane from "./TopPane";
import LowerPane from "./LowerPane";

const HomePage = (props) => {
  const { events, setEvents } = props;
  // console.log("events");
  // console.log(events);

  // function addEvent() {
  //   // make api call
  //   // get response

  //   fetch("http://localhost:8080/allevents")
  //     .then((res) => res.json())
  //     .then((json) => setEvents([...props.events, ...json]));
  // }
  return (
    <div className="homePage">
      <TopPane events={props.events} setEvents={props.setEvents} user = {props.user} setUser={props.setUser} />
      <LowerPane events={props.events} setEvents={props.setEvents} />
    </div>
  );
};

export default HomePage;
