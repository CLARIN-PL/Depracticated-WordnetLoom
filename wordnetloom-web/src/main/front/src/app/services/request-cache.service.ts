import { Injectable } from '@angular/core';
import {HttpEvent, HttpRequest, HttpResponse} from '@angular/common/http';

// setting to 30 minutes
const maxAge = 1000 * 60 * 30;

@Injectable()
export class RequestCacheService {

  cache = new Map();

  constructor() {
    // setting interval to be one minute less then maxAge
    setInterval(() => this.deleteExpiredEntries(), maxAge - (1000 * 60));
  }

  private deleteExpiredEntries() {
    const expired = Date.now() - maxAge;
    this.cache.forEach(entry => {
      if (entry.lastRead < expired) {
        this.cache.delete(entry.url);
      }
    });
  }

  get(req: HttpRequest<any>): HttpEvent<any> | undefined {
    const url = req.urlWithParams;
    const cached = this.cache.get(url);

    if (!cached) {
      return undefined;
    }

    return cached.response;
  }

  put(req: HttpRequest<any>, response: HttpEvent<any>): void {
    const url = req.url;
    const entry = {
      url,
      response,
      lastRead: Date.now()
    };
    this.cache.set(url, entry);
  }

}


