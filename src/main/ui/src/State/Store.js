import { createStore, applyMiddleware } from "redux";
import reducers from "./Reducers/AllReducers"
import thunk from "redux-thunk";
import promiseMiddleware from "redux-promise";


const Store = createStore(
    reducers,
    {
        "temp" : new Set(),
    }, //this is the default state
    applyMiddleware(thunk, promiseMiddleware)
)

export { Store };