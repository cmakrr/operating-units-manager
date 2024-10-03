import { ConfigProvider, DatePicker } from "antd";
import locale from "antd/locale/ru_RU";
import dayjs from "dayjs";
import "dayjs/locale/ru";
import React, {useCallback } from "react";

dayjs.locale("ru");

const datePresets = [
  {
    label: "Позавчера",
    value: dayjs().add(-2, "d"),
  },
  {
    label: "Вчера",
    value: dayjs().add(-1, "d"),
  },
  {
    label: "Завтра",
    value: dayjs().add(+1, "d"),
  },
  {
    label: "Послезавтра",
    value: dayjs().add(+2, "d"),
  },
];

const LocaleDatePicker = ({ defaultValue, navigate, url }) => {
  const onChange = useCallback(
      (date) => {
        if (date) {
          const formattedDate = dayjs(date).format(`YYYY-MM-DD`);
          const currentDate = new URLSearchParams(window.location.search).get("date");
          if (formattedDate !== currentDate) {
            navigate(url(formattedDate));
          }
        }
      },
      [navigate, url]
  );

  return (
    <ConfigProvider locale={locale}>
      <DatePicker
        presets={datePresets}
        onChange={onChange}
        defaultValue={dayjs(defaultValue ? defaultValue : new Date())}
      />
    </ConfigProvider>
  );
};

export default LocaleDatePicker;
