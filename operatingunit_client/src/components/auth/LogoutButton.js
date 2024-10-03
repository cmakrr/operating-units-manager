import { Button, Modal, Space } from "antd";
import { LogoutOutlined } from "@ant-design/icons";
import { clientApi } from "../../const/api/clientApi";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { tokenKey } from "../../const/constants";
const LogoutButton = () => {
  const [modal, contextHolder] = Modal.useModal();

  const [redirect, setRedirect] = useState(null);
  const navigate = useNavigate();

  const logoutHandler = (e) => {
    sessionStorage.removeItem(tokenKey);
    setRedirect(clientApi.login.base);
  };

  useEffect(() => {
    if (redirect) {
      navigate(redirect.toString());
    }
  }, [navigate, redirect]);

  const logout = () => {
    modal.confirm({
      title: "Выход",
      icon: <LogoutOutlined />,
      content: "Выйти из системы?",
      cancelText: "Нет",
      okText: "Да",
      okType: "danger",
      onOk: logoutHandler,
    });
  };

  return (
    <>
      <Space>
        <Button danger onClick={logout}>
          Выйти
        </Button>
      </Space>
      {contextHolder}
    </>
  );
};

export default LogoutButton;
