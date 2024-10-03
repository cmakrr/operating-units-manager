import {Col, Layout, Row} from "antd";
import CustomHeader from "../../components/common/CustomHeader";
import UserLogin from "../../components/auth/UserLogin";

function UserLoginPage() {
    return (
        <Layout>
            <CustomHeader/>
            <Row justify="center" align="middle" style={{flexGrow: 1}}>
                <Col span={8}>
                    <UserLogin/>
                </Col>
            </Row>
        </Layout>
    );
}

export default UserLoginPage;
