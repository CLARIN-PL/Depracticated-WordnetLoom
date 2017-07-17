package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.client.Main;

public class Messages {

    public static final String QUESTION_REMOVE_UNIT = Main.getResouce("question.message.remove.unit");
    public static final String QUESTION_REMOVE_UNITS = Main.getResouce("question.message.remove.units");
    public static final String QUESTION_UNIT_HAS_RELATIONS = Main.getResouce("question.message.unit.has.relations");
    public static final String QUESTION_SET_STATUS = Main.getResouce("question.message.set.status");
    public static final String QUESTION_DELETE_SYNSET = Main.getResouce("question.message.delete.synset");
    public static final String QUESTION_DETACH_LEXICAL_UNITS_FROM_SYNSET = Main.getResouce("question.message.detach.lexical.units.form.synset");
    public static final String QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION = Main.getResouce("question.message.create.connection.from.reverse.relation");
    public static final String QUESTION_DO_YOU_WANT_TO_RECALCULATE_GRAPHS = Main.getResouce("question.message.do.you.want.to.recalculate.graphs");
    public static final String QUESTION_REMOVE_USER_DATA = Main.getResouce("question.message.remove.user.data");
    public static final String QUESTION_SURE_TO_REMOVE_ELEMENT = Main.getResouce("question.message.sure.to.remove.element");
    public static final String QUESTION_REASSIGN_RELATION_TO_SUBTYPE = Main.getResouce("question.message.reassign.relation.to.subtype");
    public static final String QUESTION_REASSIGN_TEST_TO_SUBTYPE = Main.getResouce("question.message.reassign.test.to.subtype");
    public static final String QUESTION_SURE_TO_REMOVE_TEST = Main.getResouce("question.message.sure.to.remove.test");
    public static final String QUESTION_SURE_TO_REMOVE_RELATION = Main.getResouce("question.message.sure.to.remove.relation");
    public static final String QUESTION_SURE_TO_REMOVE_REVERSE_RELATION = Main.getResouce("question.message.sure.to.remove.reverse.relation");

    public static final String ERROR_RELATION_CANT_HAVE_SUBTYPES = Main.getResouce("error.message.reation.cant.have.subtypes");
    public static final String ERROR_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET = Main.getResouce("error.message.cannot.move.all.units.from.synset");
    public static final String ERROR_CANNOT_CHANGE_STATUS = Main.getResouce("error.message.cannot.change.status");
    public static final String ERROR_INCORRECT_DOMAIN = Main.getResouce("error.incorrect.domain");
    public static final String ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_UNITS = Main.getResouce("error.no.status.change.because.of.relations.in.unit");
    public static final String ERROR_WRONG_NUMBER_FORMAT = Main.getResouce("error.wrong.number.format");
    public static final String ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_SYNSETS = Main.getResouce("error.no.status.change.because.of.relations.in.synset");
    public static final String ERROR_CANNOT_CHANGE_STATUS_RESERVED_FOR_ADMIN = Main.getResouce("error.cannot.change.status.reserved.for.admin");
    public static final String ERROR_UNABLE_TO_CONNECT_TO_SERVER = Main.getResouce("error.server.connection");
    public static final String ERROR_UNABLE_TO_CONNECT_TO_SERVER_OR_INCORRECT_AUTHORIZATION = Main.getResouce("error.server.connection.auth");

    public static final String SUCCESS_RELATION_ADDED = Main.getResouce("success.message.relation.added");
    public static final String SUCCESS_PACKAGE_RECALCULATION_COMPLETE = Main.getResouce("success.message.package.recalcualtion.complete");
    public static final String SUCCESS_CACHE_CLEANED = Main.getResouce("success.message.cache.cleaned");
    public static final String SUCCESS_SELECTED_RELATION_DELETED = Main.getResouce("success.message.selected.relation.deleted");
    public static final String SUCCESS_SELECTED_RELATION_WITH_REVERSED_DELETED = Main.getResouce("success.message.selected.relation.with.reversed.deleted");

    public static final String FAILURE_UNABLE_TO_ADD_RELATION = Main.getResouce("failure.message.unable.to.add.relation");
    public static final String FAILURE_RELATION_EXISTS = Main.getResouce("failure.message.relation.exists");
    public static final String FAILURE_SOURCE_SYNSET_SAME_AS_TARGET = Main.getResouce("failure.message.source.synset.same.as.target");
    public static final String FAILURE_SOURCE_UNIT_SAME_AS_TARGET = Main.getResouce("failure.message.source.unit.same.as.target");
    public static final String FAILURE_UNIT_EXISTS = Main.getResouce("failure.message.unit.exists");

    public static final String INFO_ADD_USER_DATA_FIRST = Main.getResouce("info.message.add.user.data");
    public static final String INFO_UNIT_ALREADY_ASSIGNED_TO_SYNSET = Main.getResouce("info.message.unit.already.assigned.to.synset");
    public static final String INFO_RESTART_APPLICATION = Main.getResouce("info.message.restart.application");
    public static final String INVALID_CHAR_IN_LEXICON_STRING = Main.getResouce("error.message.invalid.char.in.lexicon");
    public static final String WRONG_LINK = Main.getResouce("error.message.wrong.link");
    public static final String SELECT_DOMAIN = Main.getResouce("error.select.domain");
    public static final String SELECT_POS = Main.getResouce("error.select.pos");
    public static final String SELECT_LEXICON = Main.getResouce("error.select.lexicon");
    public static final String SELECT_LEMMA = Main.getResouce("error.select.lemma");

}
