/**
 * Author : Shubham
 * Purpose : Page which is displayed when the user wants to check out their information
 */
import React, { Component } from "react";
import UserInfoPane from "./UserInfoPane";
import UserActionPanel from "./UserActionPanel";
const UserInfo = (props) => {
  const sessionId = localStorage.getItem("sessionid");
  const [userEvents, setUserEvents] = React.useState([]);
  // console.log("---")
  // console.log(userEvents)

  //we get the user info every time the user comes to this page
  React.useEffect(() => {
    console.log(sessionId);
    fetch("http://localhost:8080/userinfo?sessionid=" + sessionId, {
      method: "GET",
      // mode: "no-cors",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true",
      },
    })
      .then((res) => res.json())
      .then((json) => {
        let res = json;
        /**
         * A sucessful response will just be the user info and will
         * not have the okay key
         * If user session has expired, we redirect them to the homepage
         */
        //   console.log(res);
        if (Array.isArray(res)) {
          localStorage.clear();
          alert("Session has expired, please login again");
          window.location.href = "/";
          return;
        } else {
          localStorage.setItem("firstName", res.firstName);
          localStorage.setItem("lastName", res.lastName);
          // localStorage.setItem("preferredName", res.preferredName);
          localStorage.setItem("email", res.email);
        }
      });
  }, []);

  //we also need to get a list of events which the user will be attending
  React.useEffect(() => {
    fetch("http://localhost:8080/userevents?sessionid=" + sessionId)
      .then((res) => res.json())
      .then((json) => {
        console.log("json");
        console.log(json);
        setUserEvents(json);
      });
  }, []);

  // console.log("events")
  // console.log(userEvents)

  /**
   * This component is broken down into two further components, the infoPane and the actionPanel
   */
  return (
    <div className="displayScreen">
      <UserInfoPane sessionId={localStorage.getItem("sessionid")} />
      <UserActionPanel
        sessionId={localStorage.getItem("sessionid")}
        userEvents={userEvents}
      />
    </div>
  );
};

export default UserInfo;
