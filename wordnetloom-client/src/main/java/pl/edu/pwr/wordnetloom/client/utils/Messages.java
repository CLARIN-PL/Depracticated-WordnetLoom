package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;

public class Messages {

    public static final String QUESTION_REMOVE_UNIT = LocalisationManager.getInstance().getResource("question.message.remove.unit");
    public static final String QUESTION_REMOVE_UNITS = LocalisationManager.getInstance().getResource("question.message.remove.units");
    public static final String QUESTION_UNIT_HAS_RELATIONS = LocalisationManager.getInstance().getResource("question.message.unit.has.relations");
    public static final String QUESTION_SET_STATUS = LocalisationManager.getInstance().getResource("question.message.set.status");
    public static final String QUESTION_DELETE_SYNSET = LocalisationManager.getInstance().getResource("question.message.delete.synset");
    public static final String QUESTION_DETACH_LEXICAL_UNITS_FROM_SYNSET = LocalisationManager.getInstance().getResource("question.message.detach.lexical.units.form.synset");
    public static final String QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION = LocalisationManager.getInstance().getResource("question.message.create.connection.from.reverse.relation");
    public static final String QUESTION_DO_YOU_WANT_TO_RECALCULATE_GRAPHS = LocalisationManager.getInstance().getResource("question.message.do.you.want.to.recalculate.graphs");
    public static final String QUESTION_REMOVE_USER_DATA = LocalisationManager.getInstance().getResource("question.message.remove.user.data");
    public static final String QUESTION_SURE_TO_REMOVE_ELEMENT = LocalisationManager.getInstance().getResource("question.message.sure.to.remove.element");
    public static final String QUESTION_REASSIGN_RELATION_TO_SUBTYPE = LocalisationManager.getInstance().getResource("question.message.reassign.relation.to.subtype");
    public static final String QUESTION_REASSIGN_TEST_TO_SUBTYPE = LocalisationManager.getInstance().getResource("question.message.reassign.test.to.subtype");
    public static final String QUESTION_SURE_TO_REMOVE_TEST = LocalisationManager.getInstance().getResource("question.message.sure.to.remove.test");
    public static final String QUESTION_SURE_TO_REMOVE_RELATION = LocalisationManager.getInstance().getResource("question.message.sure.to.remove.relation");
    public static final String QUESTION_SURE_TO_REMOVE_REVERSE_RELATION = LocalisationManager.getInstance().getResource("question.message.sure.to.remove.reverse.relation");

    public static final String ERROR_RELATION_CANT_HAVE_SUBTYPES = LocalisationManager.getInstance().getResource("error.message.reation.cant.have.subtypes");
    public static final String ERROR_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET = LocalisationManager.getInstance().getResource("error.message.cannot.move.all.units.from.synset");
    public static final String ERROR_CANNOT_CHANGE_STATUS = LocalisationManager.getInstance().getResource("error.message.cannot.change.status");
    public static final String ERROR_INCORRECT_DOMAIN = LocalisationManager.getInstance().getResource("error.incorrect.domain");
    public static final String ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_UNITS = LocalisationManager.getInstance().getResource("error.no.status.change.because.of.relations.in.unit");
    public static final String ERROR_WRONG_NUMBER_FORMAT = LocalisationManager.getInstance().getResource("error.wrong.number.format");
    public static final String ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_SYNSETS = LocalisationManager.getInstance().getResource("error.no.status.change.because.of.relations.in.synset");
    public static final String ERROR_CANNOT_CHANGE_STATUS_RESERVED_FOR_ADMIN = LocalisationManager.getInstance().getResource("error.cannot.change.status.reserved.for.admin");
    public static final String ERROR_UNABLE_TO_CONNECT_TO_SERVER = LocalisationManager.getInstance().getResource("error.server.connection");
    public static final String ERROR_UNABLE_TO_CONNECT_TO_SERVER_OR_INCORRECT_AUTHORIZATION = LocalisationManager.getInstance().getResource("error.server.connection.auth");

    public static final String SUCCESS_RELATION_ADDED = LocalisationManager.getInstance().getResource("success.message.relation.added");
    public static final String SUCCESS_PACKAGE_RECALCULATION_COMPLETE = LocalisationManager.getInstance().getResource("success.message.package.recalcualtion.complete");
    public static final String SUCCESS_CACHE_CLEANED = LocalisationManager.getInstance().getResource("success.message.cache.cleaned");
    public static final String SUCCESS_SELECTED_RELATION_DELETED = LocalisationManager.getInstance().getResource("success.message.selected.relation.deleted");
    public static final String SUCCESS_SELECTED_RELATION_WITH_REVERSED_DELETED = LocalisationManager.getInstance().getResource("success.message.selected.relation.with.reversed.deleted");

    public static final String FAILURE_UNABLE_TO_ADD_RELATION = LocalisationManager.getInstance().getResource("failure.message.unable.to.add.relation");
    public static final String FAILURE_RELATION_EXISTS = LocalisationManager.getInstance().getResource("failure.message.relation.exists");
    public static final String FAILURE_SOURCE_SYNSET_SAME_AS_TARGET = LocalisationManager.getInstance().getResource("failure.message.source.synset.same.as.target");
    public static final String FAILURE_SOURCE_UNIT_SAME_AS_TARGET = LocalisationManager.getInstance().getResource("failure.message.source.unit.same.as.target");
    public static final String FAILURE_UNIT_EXISTS = LocalisationManager.getInstance().getResource("failure.message.unit.exists");

    public static final String INFO_ADD_USER_DATA_FIRST = LocalisationManager.getInstance().getResource("info.message.add.user.data");
    public static final String INFO_UNIT_ALREADY_ASSIGNED_TO_SYNSET = LocalisationManager.getInstance().getResource("info.message.unit.already.assigned.to.synset");
    public static final String INFO_RESTART_APPLICATION = LocalisationManager.getInstance().getResource("info.message.restart.application");
    public static final String INVALID_CHAR_IN_LEXICON_STRING = LocalisationManager.getInstance().getResource("error.message.invalid.char.in.lexicon");
    public static final String WRONG_LINK = LocalisationManager.getInstance().getResource("error.message.wrong.link");
    public static final String SELECT_DOMAIN = LocalisationManager.getInstance().getResource("error.select.domain");
    public static final String SELECT_POS = LocalisationManager.getInstance().getResource("error.select.pos");
    public static final String SELECT_LEXICON = LocalisationManager.getInstance().getResource("error.select.lexicon");
    public static final String SELECT_LEMMA = LocalisationManager.getInstance().getResource("error.select.lemma");

}
