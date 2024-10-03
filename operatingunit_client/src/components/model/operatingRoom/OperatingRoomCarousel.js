import React from "react";
import { Carousel } from "antd";
import { blueColor, primaryColor } from "../../../const/constants";

const freeRoomStyle = {
  margin: 0,
  height: "160px",
  color: "#fff",
  lineHeight: "160px",
  textAlign: "center",
  background: primaryColor,
};

const roomStyle = {
  margin: 0,
  height: "160px",
  color: "#fff",
  lineHeight: "160px",
  textAlign: "center",
  backgroundColor: blueColor,
};

const carouselContainerStyle = {
  width: "100%",
};

const OperatingRoomCarousel = ({ rooms }) => {
  return (
    <div style={carouselContainerStyle}>
      <Carousel arrows infinite={false} autoplay={true}>
        {rooms?.map((room, index) => (
          <div key={index} style={{ width: "100%" }}>
            {!room?.currentOperation ? (
              <h3 style={freeRoomStyle}>{room?.name}</h3>
            ) : (
              <h3 style={roomStyle}>{room?.name}</h3>
            )}
          </div>
        ))}
      </Carousel>
    </div>
  );
};

export default OperatingRoomCarousel;
