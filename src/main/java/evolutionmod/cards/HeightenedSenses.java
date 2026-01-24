package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene2;
import evolutionmod.patches.EvolutionEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HeightenedSenses
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:HeightenedSenses";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HeightenedSenses.png";
    private static final int COST = 0;
    private static final int FORM_DRAW_AMOUNT = 1;

    public HeightenedSenses() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int draw = 0;
        if (isPlayerInTheseForms(BeastGene2.ID)) {
            draw += FORM_DRAW_AMOUNT;
        }
        addToBot(new HeightenedSensesAction(p, draw));

        formEffect(BeastGene2.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene2.ID)) {
            this.glowColor = BeastGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public static class HeightenedSensesAction extends AbstractGameAction {
        int add;
        public HeightenedSensesAction(AbstractPlayer player, int addAttacks) {
            this.source = player;
            this.target = player;
            this.duration = 0.001F;
            this.add = addAttacks;
        }

        public void update() {
            int draw = this.add;

            List<AbstractCard> discard = AbstractDungeon.player.hand.group.stream()
                    .filter(c -> c.type != CardType.ATTACK)
                    .collect(Collectors.toList());
            draw += discard.size();
            discard.forEach(c -> {
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            });

            CardGroup drawpile = AbstractDungeon.player.drawPile;
            List<AbstractCard> attacks = drawpile.getAttacks().group.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
                        Collections.shuffle(l, AbstractDungeon.shuffleRng.random);
                        return l.stream();
                    }))
                    .limit(draw)
                    .collect(Collectors.toList());
            if (attacks.size() > 0) {
                attacks.forEach(c -> {
                    drawpile.removeCard(c);
                    drawpile.addToTop(c);
                });
                addToTop(new DrawCardAction(attacks.size()));
            }
            this.isDone = true;

        }
    }
}