import React from "react";
import { ConfigProvider, DatePicker } from "antd";
import dayjs from "dayjs";
import locale from "antd/locale/ru_RU";
const { RangePicker } = DatePicker;

const rangePresets = [
  {
    label: "Сегодня",
    value: [dayjs().startOf("day"), dayjs().endOf("day")],
  },
  {
    label: "Вчера",
    value: [
      dayjs().subtract(1, "d").startOf("day"),
      dayjs().subtract(1, "d").endOf("day"),
    ],
  },
  {
    label: "Позавчера",
    value: [
      dayjs().subtract(2, "d").startOf("day"),
      dayjs().subtract(2, "d").endOf("day"),
    ],
  },
  {
    label: "Последние 7 дней",
    value: [dayjs().add(-7, "d"), dayjs()],
  },
  {
    label: "Последние 14 дней",
    value: [dayjs().add(-14, "d"), dayjs()],
  },
  {
    label: "Последние 30 дней",
    value: [dayjs().add(-30, "d"), dayjs()],
  },
  {
    label: "Последние 60 дней",
    value: [dayjs().add(-60, "d"), dayjs()],
  },
  {
    label: "Последние 90 дней",
    value: [dayjs().add(-90, "d"), dayjs()],
  },
];

const LocalDateIntervalPicker = ({
  defaultStartValue,
  defaultEndValue,
  navigate,
  url,
}) => {
  const onRangeChange = (dates) => {
    if (dates) {
      navigate(
        url(
          dayjs(dates[0]).format(`YYYY-MM-DD`),
          dayjs(dates[1]).format(`YYYY-MM-DD`),
        ),
      );
    }
  };

  const disabledDate = (current) => {
    return current && current > dayjs().endOf("day");
  };

  return (
    <ConfigProvider locale={locale}>
      <RangePicker
        presets={rangePresets}
        onChange={onRangeChange}
        disabledDate={disabledDate}
        defaultValue={
          defaultStartValue && defaultEndValue
            ? [dayjs(defaultStartValue), dayjs(defaultEndValue)]
            : [dayjs(new Date()), dayjs(new Date())]
        }
      />
    </ConfigProvider>
  );
};
export default LocalDateIntervalPicker;
