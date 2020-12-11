package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;

public class FeatherStorm
        extends BaseEvoCard {
//        extends BaseEvoCard implements StartupCard {
    public static final String ID = "evolutionmod:FeatherStorm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HarpyAtt.png";
    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int FEATHER_AMT = 2;
    private static final int UPGRADE_FEATHER_AMT = 1;

    private boolean shouldSpawnFeathers;

    public FeatherStorm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = FEATHER_AMT;
        this.shouldSpawnFeathers = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (this.shouldSpawnFeathers) {
            addToBot(new MakeTempCardInDrawPileAction(new Feather(), this.magicNumber, true, true));
            this.shouldSpawnFeathers = false;
        }
    }

//    @Override
//    public boolean atBattleStartPreDraw() {
//        addToBot(new MakeTempCardInDrawPileAction(new Feather(), this.magicNumber, true, true));
//        return false;
//    }

    @Override
    public AbstractCard makeCopy() {
        return new FeatherStorm();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        FeatherStorm copy = (FeatherStorm) super.makeStatEquivalentCopy();
        copy.shouldSpawnFeathers = this.shouldSpawnFeathers;
        return copy;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FEATHER_AMT);
        }
    }
}