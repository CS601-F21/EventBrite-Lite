/**
 * Author : Shubham Pareek
 * Purpose : Component for the logout button
 */
import react, { Component, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";

const LogoutComponent = (props) => {
  const user = props.user;
  const setUser = props.setUser;

  //clicking the button calls the /logout uri and deactivate the session
  //we also clear the localstorage if the user decides to log out
  const logout = () => {
    fetch("http://localhost:8080/logout")
      .then(() => localStorage.clear())
      .then(() => window.location.reload());
  };

  //if the user wants to check out their info they will click on the button and will be redirected to some other page
  //this block of code ensures that happens
  function redirectUser() {
    const sessionId = localStorage.getItem("sessionid");
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
        console.log(res);
        /**
         * A sucessful response will just be the user info and will
         * not have the okay key
         */
        if ("ok" in res) {
          localStorage.clear();
          alert("Session has expired, please login again");
          window.location.reload();
          return;
        } else {
          setUser(res);
          window.location.href = "/user";
        }
      });
  }

  return (
    <div className={["loginWrapper"].join(" ")}>
      <div onClick={redirectUser} className="infoTextWrapper">
        <h3 className="infoText">
          Hello {localStorage.getItem("preferredName")}
        </h3>
      </div>
      <a className={"loginbutton"} onClick={logout}>
        Logout
      </a>
    </div>
  );
};
export default LogoutComponent;
