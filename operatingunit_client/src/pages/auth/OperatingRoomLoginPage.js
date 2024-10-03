import {Col, Layout, Row} from "antd";
import OperatingRoomLogin from "../../components/auth/OperatingRoomLogin";
import Title from "antd/lib/typography/Title";
import {primaryColor} from "../../const/constants";
import CustomHeader from "../../components/common/CustomHeader";
import {Link} from "react-router-dom";
import {clientApi} from "../../const/api/clientApi";
import UserLoginPage from "./UserLoginPage";

function OperatingRoomLoginPage() {
    const urlParams = new URLSearchParams(window.location.search);
    const roomName = urlParams.get("roomName");

    return (
        <Layout>
            <CustomHeader/>
            <Title level={2} style={{marginBottom: "10px"}}>
                Операционная:
            </Title>
            <Title level={1} style={{color: primaryColor, marginTop: "1%"}}>
                {roomName}
            </Title>
            <Row justify="center" align="middle" style={{flexGrow: 1}}>
                <Col span={8}>
                    <OperatingRoomLogin login={roomName}/>
                </Col>
            </Row>
            <Link to={clientApi.login.user} component={UserLoginPage.Link}>
                Войти как менеджер
            </Link>
            <br/>
        </Layout>
    );
}

export default OperatingRoomLoginPage;
