import CustomHeader from "./CustomHeader";
import CurrentTime from "./CurrentTime";
import LogoutButton from "../auth/LogoutButton";
import React from "react";
import { Layout } from "antd";
import { getRoleFromToken } from "../../functions/Token";

const MainHeader = ({ children }) => {
  const userRole = getRoleFromToken();
  return (
    <Layout>
      <CustomHeader />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginTop: "10px",
          marginBottom: "10px",
        }}
      >
        <div></div>
        <CurrentTime />
        {userRole !== undefined && <LogoutButton />}
      </div>
      <div style={{ padding: "0 16px", width: "100%" }}>{children}</div>
    </Layout>
  );
};

export default MainHeader;
