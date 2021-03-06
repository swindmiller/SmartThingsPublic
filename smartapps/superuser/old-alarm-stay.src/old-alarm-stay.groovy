/**
 *  Alarm Stay
 *
 *  Copyright 2015 Scott Windmiller
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Old - Alarm Stay",
    namespace: "",
    author: "Scott Windmiller",
    description: "Alarm Stay mode",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Select Switch to monitor"){
        input "theSwitch", "capability.switch"
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated(settings) {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def offHandler(evt) {
   if (location.mode == "Bedtime")
    {
if(getSunriseAndSunset().sunrise.time < now() && 
                 getSunriseAndSunset().sunset.time > now()){
    log.debug "Daytime"
            setLocationMode("Home Day")
            }
            else {
    log.debug "Nighttime"
            setLocationMode("Home Night")
            }
    log.debug "Received off from ${theSwitch}"
}
}

def onHandler(evt) {
        log.debug "Bedtime"
            setLocationMode("Bedtime")
     log.debug "Received on from ${theSwitch}"
}


def modeChangeHandler(evt) {
   if (evt.value == "Bedtime")
    {
    log.debug "Changed to Stay"
        theSwitch.on()
    }
    else {
    log.debug "Changed to Not in Stay mode"
        theSwitch.off()
    }
}


def initialize() {
    subscribe(theSwitch, "switch.On", onHandler)
    subscribe(theSwitch, "switch.Off", offHandler)
    subscribe(location, modeChangeHandler)
}