import {DownOutlined, PlusOutlined, UserOutlined } from '@ant-design/icons';
import {Button, message, Input, Drawer, Modal} from 'antd';
import React, { useState, useRef } from 'react';
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import ProDescriptions from '@ant-design/pro-descriptions';
import CreateForm from './components/CreateForm';
import UpdateForm from './components/UpdateForm';
import type { MenuTableListItem } from './data.d';
import {queryRule, addOrUpdateUser, removeUser} from './service';

import styles from './index.less';
import Avatar from "antd/es/avatar";
import Image from "antd/es/image";
import Dropdown from "antd/es/dropdown/dropdown";
import Menu from "antd/es/menu";


const handleAddOrUpdate = async (fields: MenuTableListItem) => {
    const tipsTitle = fields.id?"修改":"添加";
    const hide = message.loading(`正在${tipsTitle}`);
    try {
        const { msg } = await addOrUpdateUser({ ...fields });
        hide();
        message.success(msg);
        return true;
    } catch (error) {
        hide();
        message.error(error);
        return false;
    }
};

const handleRemove = async (selectedRows: MenuTableListItem[]) => {
    const hide = message.loading('正在删除');
    if (!selectedRows) return true;
    try {
        const { msg } = await removeUser(selectedRows.map((row) => row.id));
        hide();
        message.success(msg);
        return true;
    } catch (error) {
        hide();
        message.error('删除失败，请重试');
        return false;
    }
};


const TableList: React.FC<{}> = () => {
    const [createModalVisible, handleModalVisible] = useState<boolean>(false);
    const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
    const [formValues, setFormValues] = useState({});
    const actionRef = useRef<ActionType>();
    const [row, setRow] = useState<MenuTableListItem>();
    const [selectedRowsState, setSelectedRows] = useState<MenuTableListItem[]>([]);

    const editAndDelete = (key: string | number, currentItem: MenuTableListItem) => {
        if (key === 'edit') {
            handleUpdateModalVisible(true);
            setFormValues(currentItem);
        } else if (key === 'delete') {
            Modal.confirm({
                title: '删除菜单',
                content: '确定删除该菜单吗？',
                okText: '确认',
                cancelText: '取消',
                onOk: () => {
                    handleRemove([currentItem])
                }
            });
        }
    };

    const updateEnabled = (selectedRows: MenuTableListItem[],enabled:boolean) =>{
        selectedRows.forEach((item)=>{
            handleAddOrUpdate({id:item.id,enabled:enabled})
        })
    };

    const MoreBtn: React.FC<{
        item: MenuTableListItem;
    }> = ({ item }) => (
        <Dropdown
            overlay={
                <Menu onClick={({ key }) => editAndDelete(key, item)}>
                    <Menu.Item key="edit">编辑</Menu.Item>
                    <Menu.Item key="delete">删除</Menu.Item>
                </Menu>
            }
        >
            <a>
                更多 <DownOutlined />
            </a>
        </Dropdown>
    );

    const columns: ProColumns<MenuTableListItem>[] = [
        {
            title: '编码',
            dataIndex: 'code',
            tip: '编码是唯一的',
            sorter: true,
            formItemProps: {
                rules: [
                    {
                        required: true,
                        message: '编码为必填项',
                    },
                ],
            },
            render: (dom, entity) => {
                return <a onClick={() => setRow(entity)}>{dom}</a>;
            },
        },
        },
        {
            title: '父ID',
            sorter: true,
            dataIndex: 'parentId',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '名称',
            sorter: true,
            dataIndex: 'name',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: 'Url',
            sorter: true,
            dataIndex: 'url',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '路由',
            sorter: true,
            dataIndex: 'path',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '方法',
            sorter: true,
            dataIndex: 'pathMethod',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '样式',
            sorter: true,
            dataIndex: 'css',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '排序',
            sorter: true,
            dataIndex: 'sort',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        },
        },
        {
            title: '类型',
            sorter: true,
            dataIndex: 'type',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
            filters: [
                 {
                     text: '正常',
                     value: 1,
                 },
                 {
                     text: '禁用',
                     value: 0,
                 },
             ],
            filterMultiple: false,
        },
        {
            title: '是否启用',
            sorter: true,
            dataIndex: 'enabled',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
            filters: [
                 {
                     text: '正常',
                     value: 1,
                 },
                 {
                     text: '禁用',
                     value: 0,
                 },
             ],
            filterMultiple: false,
        },
        {
            title: '租户字段',
            sorter: true,
            dataIndex: 'tenantId',
            hideInForm: false,
            hideInSearch:true,
            hideInTable:false,
        },
        {
            title: '操作',
            dataIndex: 'option',
            valueType: 'option',
            render: (_, record) => [
                <a
                    onClick={() => {
                        handleUpdateModalVisible(true);
                        setFormValues(record);
                    }}
                >
                    配置
                </a>,
                <MoreBtn key="more" item={record} />,
            ],
        },
    ];

    return (
        <PageContainer>
            <ProTable<MenuTableListItem>
                headerTitle="菜单管理"
                actionRef={actionRef}
                rowKey="id"
                search={{
                labelWidth: 120,
            }}
                toolBarRender={() => [
                <Button type="primary" onClick={() => handleModalVisible(true)}>
                    <PlusOutlined /> 新建
                </Button>,
            ]}
                request={(params, sorter, filter) => queryRule({ ...params, sorter, filter })}
                columns={columns}
                rowSelection={{
                onChange: (_, selectedRows) => setSelectedRows(selectedRows),
            }}
                />
                {selectedRowsState?.length > 0 && (
                    <FooterToolbar
                        extra={
                            <div>
                                已选择 <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a> 项&nbsp;&nbsp;
                                <span>
                被禁用的用户共 {selectedRowsState.length - selectedRowsState.reduce((pre, item) => pre + (item.enabled?1:0), 0)} 人
              </span>
                            </div>
                        }
                    >
                        <Button type="primary" danger
                                onClick={async () => {
                                    await handleRemove(selectedRowsState);
                                    setSelectedRows([]);
                                    actionRef.current?.reloadAndRest?.();
                                }}
                        >
                            批量删除
                        </Button>
                        <Button type="primary"
                                onClick={async () => {
                                    await updateEnabled(selectedRowsState,true);
                                    setSelectedRows([]);
                                    actionRef.current?.reloadAndRest?.();
                                }}
                        >批量启用</Button>
                        <Button danger
                                onClick={async () => {
                                    await updateEnabled(selectedRowsState,false);
                                    setSelectedRows([]);
                                    actionRef.current?.reloadAndRest?.();
                                }}
                        >批量禁用</Button>
                    </FooterToolbar>
                )}
                <CreateForm onCancel={() => handleModalVisible(false)} modalVisible={createModalVisible}>
                    <ProTable<MenuTableListItem, MenuTableListItem>
                    onSubmit={async (value) => {
                    const success = await handleAddOrUpdate(value);
                    if (success) {
                        handleModalVisible(false);
                        if (actionRef.current) {
                            actionRef.current.reload();
                        }
                    }
                }}
                    rowKey="id"
                    type="form"
                    columns={columns}
                    />
                </CreateForm>
                {formValues && Object.keys(formValues).length ? (
                    <UpdateForm
                        onSubmit={async (value) => {
                            const success = await handleAddOrUpdate(value);
                            if (success) {
                                handleUpdateModalVisible(false);
                                setFormValues({});
                                if (actionRef.current) {
                                    actionRef.current.reload();
                                }
                            }
                        }}
                        onCancel={() => {
                            handleUpdateModalVisible(false);
                            setFormValues({});
                        }}
                        updateModalVisible={updateModalVisible}
                        values={formValues}
                    />
                ) : null}

                <Drawer
                    width={600}
                    visible={!!row}
                    onClose={() => {
                        setRow(undefined);
                    }}
                    closable={false}
                >
                    {row?.username && (
                        <ProDescriptions<MenuTableListItem>
                            column={2}
                            title={row?.rname}
                            request={async () => ({
                            data: row || {},
                        })}
                            params={{
                            id: row?.name,
                        }}
                            columns={columns}
                            />
                            )}
                </Drawer>
        </PageContainer>
);
};

export default TableList;
