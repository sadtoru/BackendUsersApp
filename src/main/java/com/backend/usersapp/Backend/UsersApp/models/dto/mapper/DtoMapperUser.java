package com.backend.usersapp.Backend.UsersApp.models.dto.mapper;

public class DtoMapperUser {

	private static DtoMapperUser mapper;

	private DtoMapperUser() {
	}

	public static DtoMapperUser getInstance(){
		mapper = new DtoMapperUser();
		return mapper;
	}
}
