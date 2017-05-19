import {WorkLog} from './work-log';

export class WorkLogResult {
  deviceId: number;
  workLogList: WorkLog[];
  devicePower: string;
  deviceName: string;
}
