package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Aegis
        extends CustomCard {
    public static final String ID = "evolutionmod:Aegis";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 2;

    public Aegis() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
        AbstractDungeon.getMonsters().monsters.stream().forEach(mo ->
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        mo, p, new IntangiblePlayerPower(mo, 1), 1
                )));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LymeanGene()));
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == CardType.ATTACK) {
            if (!this.upgraded) {
                // If this card is discarded before this action resolves we can move it to a custom Action
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
            } else {
                AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(this));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Aegis();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            this.target = CardTarget.ALL_ENEMY;
        }
    }
}