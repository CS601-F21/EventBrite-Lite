import react, { Component, useState } from "react";
import { useSelector } from "react-redux";

const LoginPage = (props) => {
    // if(props.user != null) return <Redirect to={"/"} />

    //code is the code which we get from slack after the user gives us permission
    let code = window.location.search;
    let response;
    let sessionId;
    let clientInfo;

    //is an async function and hence returns a promise
    //used to make a request to the back-end, which then authenticates the user and responds back with 
    //the user details
    const getClientInfo = async () => {
        console.log("i is : " + code);
        response = await fetch("http://127.0.0.1:8080/sessionid");
        sessionId = await response.json();
        console.log("Got session id as ");
        console.log(sessionId);
        response = await fetch("http://127.0.0.1:8080/login"+code+"&sessionid="+sessionId.sessionId);
        let info = await response.json();    
        return info;
    };

    //we only execute this block if the user has given us permission and we have the code from slack
    if (code){
        clientInfo = getClientInfo(); //getClientInfo method returns a promise

        //once we get the clientInfo, we "then" go on and update our state
        clientInfo.then( (info) =>{
                console.log("Got client info as ");
                console.log(info);
                localStorage.setItem("firstName", info.First_Name);
                localStorage.setItem("lastName", info.Last_Name);
                localStorage.setItem("preferredName", info.Preferred_Name);
                localStorage.setItem("email", info.Email);
                localStorage.setItem("sessionid", info.sessionid)

                // document.cookie = 'JWT='+info.JWT+ 'Secure; HttpOnly; SameSite=None; Max-Age=99999999;'
                
            }
        )
    }

    return (
        <div className={["login-page"].join(" ")}>
            <a className = {"loginbutton"} href="https://slack.com/openid/connect/authorize?scope=openid%20email%20profile&amp;response_type=code&amp;redirect_uri=https%3A%2F%2F2050-138-202-198-158.ngrok.io%2Flogin&amp;client_id=2376352929024.2799261688930">
            <svg className={"loginsvg1"} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 122.8 122.8">
            <path d="M25.8 77.6c0 7.1-5.8 12.9-12.9 12.9S0 84.7 0 77.6s5.8-12.9 12.9-12.9h12.9v12.9zm6.5 0c0-7.1 5.8-12.9 12.9-12.9s12.9 5.8 12.9 12.9v32.3c0 7.1-5.8 12.9-12.9 12.9s-12.9-5.8-12.9-12.9V77.6z" fill="#e01e5a"></path>
            <path d="M45.2 25.8c-7.1 0-12.9-5.8-12.9-12.9S38.1 0 45.2 0s12.9 5.8 12.9 12.9v12.9H45.2zm0 6.5c7.1 0 12.9 5.8 12.9 12.9s-5.8 12.9-12.9 12.9H12.9C5.8 58.1 0 52.3 0 45.2s5.8-12.9 12.9-12.9h32.3z" fill="#36c5f0"></path>
            <path d="M97 45.2c0-7.1 5.8-12.9 12.9-12.9s12.9 5.8 12.9 12.9-5.8 12.9-12.9 12.9H97V45.2zm-6.5 0c0 7.1-5.8 12.9-12.9 12.9s-12.9-5.8-12.9-12.9V12.9C64.7 5.8 70.5 0 77.6 0s12.9 5.8 12.9 12.9v32.3z" fill="#2eb67d"></path>
            <path d="M77.6 97c7.1 0 12.9 5.8 12.9 12.9s-5.8 12.9-12.9 12.9-12.9-5.8-12.9-12.9V97h12.9zm0-6.5c-7.1 0-12.9-5.8-12.9-12.9s5.8-12.9 12.9-12.9h32.3c7.1 0 12.9 5.8 12.9 12.9s-5.8 12.9-12.9 12.9H77.6z" fill="#ecb22e"></path>
            </svg>Sign in with Slack</a>
        </div>
    )
}
export default LoginPage;