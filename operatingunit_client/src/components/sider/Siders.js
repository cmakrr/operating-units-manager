import React, { useState } from "react";
import {
  AlertOutlined,
  EyeOutlined,
  FileAddOutlined,
  FileOutlined,
  FileSearchOutlined, MedicineBoxOutlined,
  PieChartOutlined,
  RobotOutlined,
  TeamOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Breadcrumb, Layout, Menu } from "antd";
import { useNavigate } from "react-router-dom";
import { managerMenuItems, menuItemsKeyRoutes } from "../../const/constants";
import dayjs from "dayjs";
import "dayjs/locale/ru";
import { getRoleFromToken } from "../../functions/Token";

dayjs.locale("ru");
const { Content, Footer, Sider } = Layout;

function getItem(label, key, icon, children) {
  return {
    key,
    icon,
    children,
    label,
  };
}

const handleClick = (e, navigate) => {
  if (e.key === managerMenuItems.plan.view.key) {
    navigate(
      `${menuItemsKeyRoutes[e.key](dayjs(new Date()).format(`YYYY-MM-DD`))}`,
    );
  } else if (e.key === managerMenuItems.statistics.key) {
    const today = dayjs(new Date()).format(`YYYY-MM-DD`);
    navigate(`${menuItemsKeyRoutes[e.key](today, today)}`);
  } else if (managerMenuItems.users.main.list.includes(e.key)) {
    navigate(
      `${menuItemsKeyRoutes[managerMenuItems.users.main.key](e.key.toLowerCase())}`,
    );
  } else {
    navigate(`${menuItemsKeyRoutes[e.key]}`);
  }
};

export const ManagerSider = ({ children, breadcrumb, header, defaultKey }) => {
  const navigate = useNavigate();
  const [collapsed, setCollapsed] = useState(false);

  const userRole = getRoleFromToken();

  const menuItems = [
    getItem(
      managerMenuItems.monitoring.label,
      managerMenuItems.monitoring.key,
      <EyeOutlined />,
    ),
    getItem(
      managerMenuItems.plan.main.label,
      managerMenuItems.plan.main.key,
      <FileOutlined />,
      [
        (userRole === managerMenuItems.users.general_manager.key ||
          userRole === managerMenuItems.users.admin.key) &&
          getItem(
            managerMenuItems.plan.load.label,
            managerMenuItems.plan.load.key,
            <FileAddOutlined />,
          ),
        getItem(
          managerMenuItems.plan.view.label,
          managerMenuItems.plan.view.key,
          <FileSearchOutlined />,
        ),
      ],
    ),
    (userRole === managerMenuItems.users.general_manager.key ||
      userRole === managerMenuItems.users.admin.key) &&
      getItem(
        managerMenuItems.operatingRooms.label,
        managerMenuItems.operatingRooms.key,
        <AlertOutlined />,
      ),
    (userRole === managerMenuItems.users.general_manager.key ||
      userRole === managerMenuItems.users.admin.key) &&
      getItem(
        managerMenuItems.users.main.label,
        managerMenuItems.users.main.key,
        <TeamOutlined />,
        [
          userRole === managerMenuItems.users.admin.key &&
            getItem(
              managerMenuItems.users.tracker.label,
              managerMenuItems.users.tracker.key,
              <RobotOutlined />,
            ),
          (userRole === managerMenuItems.users.manager.key ||
            userRole === managerMenuItems.users.general_manager.key ||
            userRole === managerMenuItems.users.admin.key) &&
            getItem(
              managerMenuItems.users.manager.label,
              managerMenuItems.users.manager.key,
            ),
          (userRole === managerMenuItems.users.general_manager.key ||
            userRole === managerMenuItems.users.admin.key) &&
            getItem(
              managerMenuItems.users.general_manager.label,
              managerMenuItems.users.general_manager.key,
            ),
          userRole === managerMenuItems.users.admin.key &&
            getItem(
              managerMenuItems.users.admin.label,
              managerMenuItems.users.admin.key,
              <UserOutlined />,
            ),
        ],
      ),
    (userRole === managerMenuItems.users.general_manager.key ||
        userRole === managerMenuItems.users.admin.key) &&
    getItem(managerMenuItems.patients.label,
        managerMenuItems.patients.key,
        <MedicineBoxOutlined/>),
    getItem(
      managerMenuItems.statistics.label,
      managerMenuItems.statistics.key,
      <PieChartOutlined />,
    ),
  ];

  return (
    <Layout
      style={{
        minHeight: "100vh",
      }}
    >
      <Sider
        theme={"light"}
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
        overflow={"auto"}
        height={"100vh"}
        position={"fixed"}
      >
        <Menu
          theme={"light"}
          defaultSelectedKeys={defaultKey}
          mode="inline"
          items={menuItems}
          onClick={(e) => handleClick(e, navigate)}
        />
      </Sider>
      <Layout>
        <Content
          style={{
            margin: "0 16px",
          }}
        >
          {header}
          <Breadcrumb
            style={{
              margin: "16px 0",
            }}
          >
            {breadcrumb?.map((item, index) => (
              <Breadcrumb.Item key={index}>{item}</Breadcrumb.Item>
            ))}
          </Breadcrumb>
          {children}
        </Content>
        <Footer
          style={{
            textAlign: "center",
          }}
        ></Footer>
      </Layout>
    </Layout>
  );
};
