import {Button, Form, Input} from "antd";
import {serverApi} from "../../const/api/serverApi";
import {signIn} from "../../request/UserRequests";
import {clientApi} from "../../const/api/clientApi";
import {CenterCard} from "../card/Cards";

const UserLogin = () => {
    return (
        <CenterCard>
            <Form
                name="basic"
                initialValues={{
                    remember: true,
                }}
                onFinish={(values) =>
                    signIn(serverApi.login, values, clientApi.manager.monitoring)
                }
                autoComplete="off"
            >
                <Form.Item
                    label="Логин"
                    name="login"
                    rules={[
                        {
                            required: true,
                            message: "Пожалуйста, введите ваш логин!",
                        },
                    ]}
                >
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

export default UserLogin;
