/**
 * Author : Shubham Pareek
 * Purpose : Home page component
 */
import react, { Component, useState } from "react";
import { useSelector } from "react-redux";
import React from "react";
import LoginComponent from "./LoginComponent";
import TopPane from "./TopPane";
import LowerPane from "./LowerPane";

const HomePage = (props) => {
  //getting the required props
  const { events, setEvents } = props;

  /**
   * The homepage is broken down into two parts, the TopPane and the LowerPane
   * The TopPane is where we have the login button and the search bar and the lower pane
   * is where we display all the events
   */
  return (
    <div className="homePage">
      <TopPane
        events={props.events}
        setEvents={props.setEvents}
        user={props.user}
        setUser={props.setUser}
      />
      <LowerPane events={props.events} setEvents={props.setEvents} />
    </div>
  );
};

export default HomePage;
