import MainHeader from "../../components/common/MainHeader";
import { ManagerSider } from "../../components/sider/Siders";
import React from "react";
import FileLoader from "../../components/operationPlan/FileLoader";
import { managerMenuItems } from "../../const/constants";

function OperationsPlanLoadPage() {
  return (
    <MainHeader>
      <div style={{ overflowX: "auto", width: "100%" }}>
        <ManagerSider
          breadcrumb={[
            managerMenuItems.plan.main.label,
            managerMenuItems.plan.load.label,
          ]}
          defaultKey={managerMenuItems.plan.load.key}
          children={<FileLoader />}
        ></ManagerSider>
      </div>
    </MainHeader>
  );
}

export default OperationsPlanLoadPage;
