import http from "@/utils/axios";

//日志分页
export const apiJobList = (param) => {
  return http({
    method: 'GET',
    url: '/job/list',
    data: param
  })
}

//编辑任务
export const apiEditJob = (param) => {
  return http({
    method: 'POST',
    url: '/job/edit',
    data: param
  })
}