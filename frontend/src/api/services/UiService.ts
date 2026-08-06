/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Mappings } from '../models/Mappings';
import type { ValidationRequest } from '../models/ValidationRequest';
import type { ValidationResponse } from '../models/ValidationResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UiService {
    /**
     * Validates a policy against an HTTP test request or an arbitrary JSON payload
     * Validates a policy by evaluating it against a provided input. Two input modes are supported (mutually exclusive): (1) HTTP mode — provide a `testRequest` with method, host, path, etc. for evaluating policies that target HTTP API gateway requests. (2) JSON mode — provide a `jsonInput` with an arbitrary JSON `payload` and optional `subject` for evaluating policies against non-HTTP data. The policy's own JSON-LD context determines which evaluation path is used. Existing requests that only supply `testRequest` continue to work unchanged.
     *
     * @param requestBody
     * @returns ValidationResponse Validation result
     * @throws ApiError
     */
    public static validatePolicy(
        requestBody: ValidationRequest,
    ): CancelablePromise<ValidationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/validate',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Gets the supported by the PAP.
     * @returns Mappings Successfully retrieved the Mappings.
     * @throws ApiError
     */
    public static getMappings(): CancelablePromise<Mappings> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/mappings',
        });
    }
}
