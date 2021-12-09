import React, { Component } from "react";
import UserInfoPane from './UserInfoPane';
import UserActionPane from './UserActionPane';
const UserInfo = (props) => {
    const sessionId = localStorage.getItem("sessionid");
    React.useEffect(() => {
        console.log(sessionId);
        fetch("http://localhost:8080/userinfo?sessionid="+sessionId, {
          method: "GET",
          // mode: "no-cors",
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Credentials": "true"
          },
        })
        .then ((res) => res.json())
        .then ((json) => {
          let res = json;
          /**
           * A sucessful response will just be the user info and will
           * not have the okay key
           * If user session has expired, we redirect them to the homepage
           */
        //   console.log(res);
          if (Array.isArray(res)){
            localStorage.clear(); 
            alert("Session has expired, please login again")
            window.location.href = '/';
            return;
          } else {
            localStorage.setItem("firstName", res.firstName);
            localStorage.setItem("lastName", res.lastName);
            localStorage.setItem("preferredName", res.preferredName);
            localStorage.setItem("email", res.email);
          }    
        })
      }, []);

    return (
        <div>
            <UserInfoPane sessionId = {localStorage.getItem("session_id")}/>
            <UserActionPane sessionId = {localStorage.getItem("sessionid")}/>
        </div>
    )
}

export default UserInfo;