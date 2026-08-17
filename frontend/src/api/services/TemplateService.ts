/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Template } from '../models/Template';
import type { TemplateCreate } from '../models/TemplateCreate';
import type { TemplateList } from '../models/TemplateList';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class TemplateService {
    /**
     * Create a new policy template
     * Creates a new policy template with placeholder definitions. Templates contain an ODRL policy skeleton with {{PLACEHOLDER}} tokens that can be filled in later to create concrete policies.
     *
     * @param requestBody
     * @returns Template Template created successfully
     * @throws ApiError
     */
    public static createTemplate(
        requestBody: TemplateCreate,
    ): CancelablePromise<Template> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/template',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * List all general policy templates
     * Returns a paginated list of general (non-service-scoped) policy templates.
     *
     * @param page
     * @param pageSize
     * @returns TemplateList Successfully retrieved templates
     * @throws ApiError
     */
    public static getTemplates(
        page?: number,
        pageSize: number = 25,
    ): CancelablePromise<TemplateList> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/template',
            query: {
                'page': page,
                'pageSize': pageSize,
            },
        });
    }
    /**
     * Get a policy template by ID
     * @param templateId The unique identifier of the template
     * @returns Template Successfully retrieved the template
     * @throws ApiError
     */
    public static getTemplateById(
        templateId: string,
    ): CancelablePromise<Template> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/template/{template-id}',
            path: {
                'template-id': templateId,
            },
            errors: {
                404: `No such template exists`,
            },
        });
    }
    /**
     * Update an existing policy template
     * @param templateId The unique identifier of the template
     * @param requestBody
     * @returns Template Template updated successfully
     * @throws ApiError
     */
    public static updateTemplate(
        templateId: string,
        requestBody: TemplateCreate,
    ): CancelablePromise<Template> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/template/{template-id}',
            path: {
                'template-id': templateId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `No such template exists`,
            },
        });
    }
    /**
     * Delete a policy template by ID
     * @param templateId The unique identifier of the template
     * @returns void
     * @throws ApiError
     */
    public static deleteTemplateById(
        templateId: string,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/template/{template-id}',
            path: {
                'template-id': templateId,
            },
            errors: {
                404: `No such template exists`,
            },
        });
    }
}
