/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OdrlPolicyJson } from '../models/OdrlPolicyJson';
import type { Policy } from '../models/Policy';
import type { PolicyList } from '../models/PolicyList';
import type { PolicyPath } from '../models/PolicyPath';
import type { Service } from '../models/Service';
import type { ServiceCreate } from '../models/ServiceCreate';
import type { ServiceList } from '../models/ServiceList';
import type { Uid } from '../models/Uid';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ServiceService {
    /**
     * Create a new service to group policies
     * @param requestBody
     * @returns PolicyPath The policy path
     * @throws ApiError
     */
    public static createService(
        requestBody: ServiceCreate,
    ): CancelablePromise<PolicyPath> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/service',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                409: `Service already exists`,
            },
        });
    }
    /**
     * Get all services and the path to their policy
     * @param page
     * @param pageSize
     * @returns ServiceList The services
     * @throws ApiError
     */
    public static getServices(
        page?: number,
        pageSize: number = 25,
    ): CancelablePromise<ServiceList> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/service',
            query: {
                'page': page,
                'pageSize': pageSize,
            },
        });
    }
    /**
     * Get the service with the given id
     * @param serviceId
     * @returns Service The service
     * @throws ApiError
     */
    public static getService(
        serviceId: string,
    ): CancelablePromise<Service> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/service/{service-id}',
            path: {
                'service-id': serviceId,
            },
            errors: {
                404: `No such service exists`,
            },
        });
    }
    /**
     * Delete the service with the given id and all policies below
     * @param serviceId
     * @returns void
     * @throws ApiError
     */
    public static deleteService(
        serviceId: string,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/service/{service-id}',
            path: {
                'service-id': serviceId,
            },
        });
    }
    /**
     * Creates a new policy from the given odrl-json under the service-id
     * @param serviceId
     * @param requestBody
     * @returns string The rego policy
     * @throws ApiError
     */
    public static createServicePolicy(
        serviceId: string,
        requestBody: OdrlPolicyJson,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/service/{service-id}/policy',
            path: {
                'service-id': serviceId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Get the policies in the ODRL-Format under the service-id
     * @param serviceId
     * @param page
     * @param pageSize
     * @returns PolicyList Successfully retrieved the policis.
     * @throws ApiError
     */
    public static getServicePolicies(
        serviceId: string,
        page?: number,
        pageSize: number = 25,
    ): CancelablePromise<PolicyList> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/service/{service-id}/policy',
            path: {
                'service-id': serviceId,
            },
            query: {
                'page': page,
                'pageSize': pageSize,
            },
            errors: {
                404: `No such policy exists`,
            },
        });
    }
    /**
     * Creates or overwrites the given policy under the service-id.
     * @param serviceId
     * @param id
     * @param requestBody
     * @returns string The rego policy
     * @throws ApiError
     */
    public static createServicePolicyWithId(
        serviceId: string,
        id: Uid,
        requestBody: OdrlPolicyJson,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/service/{service-id}/policy/{id}',
            path: {
                'service-id': serviceId,
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                409: `Should be returned in case the policy is not allowed to be modified`,
            },
        });
    }
    /**
     * Return the given policy by its ID under the service-id.
     * @param serviceId
     * @param id
     * @returns Policy Successfully retrieved the policy.
     * @throws ApiError
     */
    public static getServicePolicyById(
        serviceId: string,
        id: Uid,
    ): CancelablePromise<Policy> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/service/{service-id}/policy/{id}',
            path: {
                'service-id': serviceId,
                'id': id,
            },
            errors: {
                404: `No such policy exists`,
            },
        });
    }
    /**
     * Delete the given policy under the service-id.
     * @param serviceId
     * @param id
     * @returns void
     * @throws ApiError
     */
    public static deleteServicePolicyById(
        serviceId: string,
        id: Uid,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/service/{service-id}/policy/{id}',
            path: {
                'service-id': serviceId,
                'id': id,
            },
            errors: {
                404: `No such policy exists`,
            },
        });
    }
    /**
     * Return the given policy by its ID under the service-id.
     * @param serviceId
     * @param id
     * @returns Policy Successfully retrieved the policy.
     * @throws ApiError
     */
    public static getServicePolicyByUid(
        serviceId: string,
        id: Uid,
    ): CancelablePromise<Policy> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/service/{service-id}/policy/odrl/{id}',
            path: {
                'service-id': serviceId,
                'id': id,
            },
            errors: {
                404: `No such policy exists`,
            },
        });
    }
    /**
     * Delete the given policy under the service-id.
     * @param serviceId
     * @param id
     * @returns void
     * @throws ApiError
     */
    public static deleteServicePolicyByUid(
        serviceId: string,
        id: Uid,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/service/{service-id}/policy/odrl/{id}',
            path: {
                'service-id': serviceId,
                'id': id,
            },
            errors: {
                404: `No such policy exists`,
            },
        });
    }
}
