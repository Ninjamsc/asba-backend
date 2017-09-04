-- Только для БиоСКУД в рамках доработки "Ведение и использование перечня типов списков"


-- DROP TABLE public.stop_lists_types;

CREATE TABLE public.stop_lists_types
(
  type_id character varying NOT NULL, -- ID типа стоп-листа
  type_name character varying NOT NULL, -- Название типа стоп-листов для вывода в интерфейсе
  is_editable boolean NOT NULL DEFAULT true, -- Доступность для редактирования и удаления
  is_visible boolean NOT NULL DEFAULT true, -- Отображается ли тип в интерфейсе БиоСКУД
  CONSTRAINT stop_lists_types_pkey PRIMARY KEY (type_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.stop_lists_types
  OWNER TO biometry;
COMMENT ON TABLE public.stop_lists_types
  IS 'Типы стоп-листов';
COMMENT ON COLUMN public.stop_lists_types.type_id IS 'ID типа стоп-листа';
COMMENT ON COLUMN public.stop_lists_types.type_name IS 'Название типа стоп-листов для вывода в интерфейсе';
COMMENT ON COLUMN public.stop_lists_types.is_editable IS 'Доступность для редактирования и удаления';
COMMENT ON COLUMN public.stop_lists_types.is_visible IS 'Отображается ли тип в интерфейсе БиоСКУД';


INSERT INTO public.stop_lists_types(type_id, type_name, is_editable, is_visible)
    VALUES ('common', 'Общий', false, false);
INSERT INTO public.stop_lists_types(type_id, type_name, is_editable, is_visible)
    VALUES ('bank', 'Банковский', false, false);
INSERT INTO public.stop_lists_types(type_id, type_name, is_editable, is_visible)
    VALUES ('employees', 'Список сотрудников', false, true);
INSERT INTO public.stop_lists_types(type_id, type_name, is_editable, is_visible)
    VALUES ('stoplist', 'Стоп-лист', false, true);

ALTER TABLE public.stop_lists ADD FOREIGN KEY(type) REFERENCES public.stop_lists_types(type_id);