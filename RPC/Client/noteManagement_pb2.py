# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: noteManagement.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='noteManagement.proto',
  package='',
  syntax='proto3',
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x14noteManagement.proto\"\xac\x01\n\x07Request\x12 \n\x06option\x18\x01 \x01(\x0e\x32\x10.Request.TypeReq\x12\x1d\n\tmatricula\x18\x02 \x01(\x0b\x32\n.Matricula\"`\n\x07TypeReq\x12\x10\n\x0c\x41\x44\x44MATRICULA\x10\x00\x12\x0b\n\x07PUTNOTA\x10\x01\x12\r\n\tPUTFALTAS\x10\x02\x12\r\n\tGETALUNOS\x10\x03\x12\x0e\n\nGETBOLETIM\x10\x04\x12\x08\n\x04\x45XIT\x10\x05\"Y\n\x05Reply\x12\x0f\n\x07message\x18\x01 \x01(\t\x12\x17\n\x07student\x18\x02 \x03(\x0b\x32\x06.Aluno\x12&\n\x0breport_card\x18\x03 \x03(\x0b\x32\x11.DiscBoletimAluno\"C\n\x10\x44iscBoletimAluno\x12\x11\n\tnome_disc\x18\x01 \x01(\t\x12\x0c\n\x04nota\x18\x02 \x01(\x02\x12\x0e\n\x06\x66\x61ltas\x18\x03 \x01(\x05\"%\n\x05\x43urso\x12\x0e\n\x06\x63odigo\x18\x01 \x01(\x05\x12\x0c\n\x04nome\x18\x02 \x01(\t\"P\n\nDisciplina\x12\x0e\n\x06\x63odigo\x18\x01 \x01(\t\x12\x0c\n\x04nome\x18\x02 \x01(\t\x12\x11\n\tprofessor\x18\x03 \x01(\t\x12\x11\n\tcod_curso\x18\x04 \x01(\x05\"l\n\tMatricula\x12\n\n\x02RA\x18\x01 \x01(\x05\x12\x16\n\x0e\x63od_disciplina\x18\x02 \x01(\t\x12\x0b\n\x03\x61no\x18\x03 \x01(\x05\x12\x10\n\x08semestre\x18\x04 \x01(\x05\x12\x0c\n\x04nota\x18\x05 \x01(\x02\x12\x0e\n\x06\x66\x61ltas\x18\x06 \x01(\x05\"E\n\x05\x41luno\x12\n\n\x02RA\x18\x01 \x01(\x05\x12\x0c\n\x04nome\x18\x02 \x01(\t\x12\x0f\n\x07periodo\x18\x03 \x01(\x05\x12\x11\n\tcod_curso\x18\x04 \x01(\x05\x32:\n\x14\x43onnectionMiddleware\x12\"\n\x0c\x43omunication\x12\x08.Request\x1a\x06.Reply\"\x00\x62\x06proto3'
)



_REQUEST_TYPEREQ = _descriptor.EnumDescriptor(
  name='TypeReq',
  full_name='Request.TypeReq',
  filename=None,
  file=DESCRIPTOR,
  create_key=_descriptor._internal_create_key,
  values=[
    _descriptor.EnumValueDescriptor(
      name='ADDMATRICULA', index=0, number=0,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
    _descriptor.EnumValueDescriptor(
      name='PUTNOTA', index=1, number=1,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
    _descriptor.EnumValueDescriptor(
      name='PUTFALTAS', index=2, number=2,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
    _descriptor.EnumValueDescriptor(
      name='GETALUNOS', index=3, number=3,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
    _descriptor.EnumValueDescriptor(
      name='GETBOLETIM', index=4, number=4,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
    _descriptor.EnumValueDescriptor(
      name='EXIT', index=5, number=5,
      serialized_options=None,
      type=None,
      create_key=_descriptor._internal_create_key),
  ],
  containing_type=None,
  serialized_options=None,
  serialized_start=101,
  serialized_end=197,
)
_sym_db.RegisterEnumDescriptor(_REQUEST_TYPEREQ)


_REQUEST = _descriptor.Descriptor(
  name='Request',
  full_name='Request',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='option', full_name='Request.option', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='matricula', full_name='Request.matricula', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _REQUEST_TYPEREQ,
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=25,
  serialized_end=197,
)


_REPLY = _descriptor.Descriptor(
  name='Reply',
  full_name='Reply',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='message', full_name='Reply.message', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='student', full_name='Reply.student', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='report_card', full_name='Reply.report_card', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=199,
  serialized_end=288,
)


_DISCBOLETIMALUNO = _descriptor.Descriptor(
  name='DiscBoletimAluno',
  full_name='DiscBoletimAluno',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='nome_disc', full_name='DiscBoletimAluno.nome_disc', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nota', full_name='DiscBoletimAluno.nota', index=1,
      number=2, type=2, cpp_type=6, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='faltas', full_name='DiscBoletimAluno.faltas', index=2,
      number=3, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=290,
  serialized_end=357,
)


_CURSO = _descriptor.Descriptor(
  name='Curso',
  full_name='Curso',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='codigo', full_name='Curso.codigo', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nome', full_name='Curso.nome', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=359,
  serialized_end=396,
)


_DISCIPLINA = _descriptor.Descriptor(
  name='Disciplina',
  full_name='Disciplina',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='codigo', full_name='Disciplina.codigo', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nome', full_name='Disciplina.nome', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='professor', full_name='Disciplina.professor', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='cod_curso', full_name='Disciplina.cod_curso', index=3,
      number=4, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=398,
  serialized_end=478,
)


_MATRICULA = _descriptor.Descriptor(
  name='Matricula',
  full_name='Matricula',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='RA', full_name='Matricula.RA', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='cod_disciplina', full_name='Matricula.cod_disciplina', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='ano', full_name='Matricula.ano', index=2,
      number=3, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='semestre', full_name='Matricula.semestre', index=3,
      number=4, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nota', full_name='Matricula.nota', index=4,
      number=5, type=2, cpp_type=6, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='faltas', full_name='Matricula.faltas', index=5,
      number=6, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=480,
  serialized_end=588,
)


_ALUNO = _descriptor.Descriptor(
  name='Aluno',
  full_name='Aluno',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='RA', full_name='Aluno.RA', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='nome', full_name='Aluno.nome', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='periodo', full_name='Aluno.periodo', index=2,
      number=3, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='cod_curso', full_name='Aluno.cod_curso', index=3,
      number=4, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=590,
  serialized_end=659,
)

_REQUEST.fields_by_name['option'].enum_type = _REQUEST_TYPEREQ
_REQUEST.fields_by_name['matricula'].message_type = _MATRICULA
_REQUEST_TYPEREQ.containing_type = _REQUEST
_REPLY.fields_by_name['student'].message_type = _ALUNO
_REPLY.fields_by_name['report_card'].message_type = _DISCBOLETIMALUNO
DESCRIPTOR.message_types_by_name['Request'] = _REQUEST
DESCRIPTOR.message_types_by_name['Reply'] = _REPLY
DESCRIPTOR.message_types_by_name['DiscBoletimAluno'] = _DISCBOLETIMALUNO
DESCRIPTOR.message_types_by_name['Curso'] = _CURSO
DESCRIPTOR.message_types_by_name['Disciplina'] = _DISCIPLINA
DESCRIPTOR.message_types_by_name['Matricula'] = _MATRICULA
DESCRIPTOR.message_types_by_name['Aluno'] = _ALUNO
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Request = _reflection.GeneratedProtocolMessageType('Request', (_message.Message,), {
  'DESCRIPTOR' : _REQUEST,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Request)
  })
_sym_db.RegisterMessage(Request)

Reply = _reflection.GeneratedProtocolMessageType('Reply', (_message.Message,), {
  'DESCRIPTOR' : _REPLY,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Reply)
  })
_sym_db.RegisterMessage(Reply)

DiscBoletimAluno = _reflection.GeneratedProtocolMessageType('DiscBoletimAluno', (_message.Message,), {
  'DESCRIPTOR' : _DISCBOLETIMALUNO,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:DiscBoletimAluno)
  })
_sym_db.RegisterMessage(DiscBoletimAluno)

Curso = _reflection.GeneratedProtocolMessageType('Curso', (_message.Message,), {
  'DESCRIPTOR' : _CURSO,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Curso)
  })
_sym_db.RegisterMessage(Curso)

Disciplina = _reflection.GeneratedProtocolMessageType('Disciplina', (_message.Message,), {
  'DESCRIPTOR' : _DISCIPLINA,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Disciplina)
  })
_sym_db.RegisterMessage(Disciplina)

Matricula = _reflection.GeneratedProtocolMessageType('Matricula', (_message.Message,), {
  'DESCRIPTOR' : _MATRICULA,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Matricula)
  })
_sym_db.RegisterMessage(Matricula)

Aluno = _reflection.GeneratedProtocolMessageType('Aluno', (_message.Message,), {
  'DESCRIPTOR' : _ALUNO,
  '__module__' : 'noteManagement_pb2'
  # @@protoc_insertion_point(class_scope:Aluno)
  })
_sym_db.RegisterMessage(Aluno)



_CONNECTIONMIDDLEWARE = _descriptor.ServiceDescriptor(
  name='ConnectionMiddleware',
  full_name='ConnectionMiddleware',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=661,
  serialized_end=719,
  methods=[
  _descriptor.MethodDescriptor(
    name='Comunication',
    full_name='ConnectionMiddleware.Comunication',
    index=0,
    containing_service=None,
    input_type=_REQUEST,
    output_type=_REPLY,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_CONNECTIONMIDDLEWARE)

DESCRIPTOR.services_by_name['ConnectionMiddleware'] = _CONNECTIONMIDDLEWARE

# @@protoc_insertion_point(module_scope)
