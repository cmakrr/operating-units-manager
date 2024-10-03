import {Button, Form, Input} from "antd";
import "../../App.css";
import {signIn,} from "../../request/UserRequests";
import {serverApi} from "../../const/api/serverApi";
import {clientApi} from "../../const/api/clientApi";
import {CenterCard} from "../card/Cards";

const OperatingRoomLogin = ({login}) => {
    return (
        <CenterCard>
            <Form
                name="basic"
                initialValues={{
                    remember: true,
                }}
                onFinish={(values) =>
                    signIn(
                        serverApi.login,
                        values,
                        clientApi.operatingRoom.operations,
                    )
                }
                autoComplete="off"
            >
                <Form.Item label="Логин" name="login" hidden initialValue={login}>
                    <Input/>
                </Form.Item>

                <Form.Item
                    label="Пароль"
                    name="password"
                    rules={[
                        {
                            required: true,
                            message: "Пожалуйста, введите ваш пароль!",
                        },
                    ]}
                >
                    <Input.Password/>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Войти
                    </Button>
                </Form.Item>
            </Form>
        </CenterCard>
    );
};

export default OperatingRoomLogin;
