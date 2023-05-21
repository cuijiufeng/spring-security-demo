import http from "@/utils/axios";

//日志分页
export const apiLogArchiveList = (param) => {
  return http({
    method: 'GET',
    url: '/log-archive/list',
    data: param
  })
}

//日志压缩文件下载
export const apiDownloadLogArchive = (param) => {
  return http({
    method: 'GET',
    url: '/log-archive/download',
    data: param,
    binary: true
  })
}