package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;

public class Feather
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Feather";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Feather.png";
    private static final int COST = 0;
    private static final int FORM_CHANGE = 1;
    private static final int UPGRADE_FORM_CHANGE = 1;

    public Feather() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORM_CHANGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new DiscardAction(p, p, this.magicNumber, false));
        formEffect(HarpyGene.ID, () ->
                addToBot(new MoveCardsAction(p.hand, p.discardPile,
                    c -> c.cardID.equals(FeatherStorm.ID), 1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Feather();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FORM_CHANGE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(HarpyGene.ID)
            && AbstractDungeon.player.discardPile.group.stream().anyMatch(c -> FeatherStorm.ID.equals(c.cardID))) {
            this.glowColor = HarpyGene.COLOR.cpy();
        } else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
    }
}